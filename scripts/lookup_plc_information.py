#!/usr/bin/env python3

from bs4 import BeautifulSoup
import os, requests, sys, json

OUTPUTFILE_TXT = "C:\\Users\\mar20266\\Documents\\SVN\BA\CyberAttackSzenarioPlatform\\tool_outputs\\look_up_plc_information_output.txt"
TIMEOUT = 5
PLC_WAGO_880 = "880"
PLC_WAGO_880_INFO_URL = "/webserv/cplcfg/state.ssi"
PLC_WAGO_8202 = "8202"
PLC_WAGO_8202_INFO_URL = "/wbm/state.php"
PLC_DEVICES_LOOKUP = [PLC_WAGO_880, PLC_WAGO_8202]

def lookup_plc_information(ip_address):
	"""Searching PLC specific information for a device with the provided ip_address"""
	print("INFO Retrieving device information for device with IP %s...\n" % (ip_address))
	write_to_file("IP %s\n" % (ip_address))
	infomation_found = False
	# Iterating over the provided list PLC_DEVICES_LOOKUP
	for plc_type in PLC_DEVICES_LOOKUP:
		information_list = crawl_plc_information(plc_type, ip_address)
		if len(information_list) != 0:
			infomation_found = True
			for info_entry in information_list:
				print(info_entry)
				write_to_file(info_entry)
			print("PLC_Type: " + plc_type)
			write_to_file("PLC_Type " + plc_type + "\n")
			return plc_type

	if not infomation_found:
		print("FAILED Could not retrive device information!\n")
		write_to_file("FAILED Could_not_retrive_device_information!\n")
		return ""

def crawl_plc_information(plc_type, device_ip):
	"""Requesting plc_type specific website for information retrieval"""
	url = ""
	if plc_type == PLC_WAGO_880:
		url = PLC_WAGO_880_INFO_URL
	if plc_type == PLC_WAGO_8202:
		url = PLC_WAGO_8202_INFO_URL
	try:
		page = requests.get("http://%s/%s" % (device_ip, url), timeout=TIMEOUT)
	except requests.exceptions.RequestException as err:
		print(str(err))
		return []

	if page.status_code == 404:
		return []

	soup = BeautifulSoup(page.content, 'html.parser')
	return crawl_device_specific_info(soup, plc_type)


def crawl_device_specific_info(soup, plc_type):
	"""Calling plc_type specific crawler function"""
	information_list = []
	if plc_type == PLC_WAGO_880:
		information_list = crawl_wago_880(soup, information_list)
	if plc_type == PLC_WAGO_8202:
		information_list = crawl_wago_8202(soup, information_list)
	return information_list


def crawl_wago_880(soup, information_list):
	"""Basic information retrieval for WAGO 750-880"""
	field = soup.find("td", text="Order number ")
	if field is not None:
		information_list.append("Order_number" + field.find_next_sibling("td").text + "\n")
	field = soup.find("td", text="Mac address ")
	if field is not None:
		information_list.append("MAC_Address" + field.find_next_sibling("td").text + "\n")
	field = soup.find("td", text="Firmware revision ")
	if field is not None:
		information_list.append("Firmware_revision" + field.find_next_sibling("td").text + "\n")
	return information_list


def crawl_wago_8202(soup, information_list):
	"""Basic information retrieval for WAGO 750-8202"""
	field = soup.find("span", id="order_number")
	if field is not None:
		information_list.append("Order_number" + field.text + "\n")
	field = soup.find("span", id="mac_address_eth0")
	if field is not None:
		information_list.append("MAC_Address" + field.text + "\n")
	field = soup.find("span", id="firmware_revision")
	if field is not None:
		information_list.append("Firmware_revision" + field.text + "\n")
	field = soup.find("span", id="product_description")
	if field is not None:
		information_list.append("Product_description" + field.text + "\n")
	return information_list

def write_to_file(string):
	with open(OUTPUTFILE_TXT,"a") as myfile:
		myfile.write(string)


if __name__ == "__main__":
	try:
		args = sys.argv[1:]
		for i in args:
			lookup_plc_information(i)
		print("FERTIG")
	except Exception:
		traceback.print_exc()

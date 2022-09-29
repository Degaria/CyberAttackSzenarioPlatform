#!/usr/bin/env python3

import sys, requests, select, json
PLC_WAGO_880 = "880"
PLC_WAGO_880_INFO_URL = "/webserv/cplcfg/state.ssi"
PLC_WAGO_880_PASS_URL = "/webserv/cplcfg/ethernet.ssi"
PLC_WAGO_880_CONFIG_URL = "/webserv/cplcfg/tcpip.ssi"
PLC_WAGO_880_RESET_URL = "/webserv/cplcfg/security.ssi"
TIMEOUT = 5
PLC_WAGO_8202 = "8202"
PLC_WAGO_8202_INFO_URL = "/wbm/state.php"
PLC_WAGO_8202_PASS_URL = "/wbm/interfaces.php"
PLC_WAGO_8202_CONFIG_URL = "/wbm/configtools.php"
MAX_RETRIES = 10
OUTPUTFILE_TXT = "C:\\Users\\mar20266\\Documents\\SVN\BA\\CyberAttackSzenarioPlatform\\CASPStorage\\tool_outputs\\change_plc_settings_output.txt"
def change_plc_settings(login, password, ip_from, plc_type, ip_to):
	"""Changing PLC settings - modyfing IP address from ip_from to ip_to"""
	print("\nChanging PLC IP from %s to %s...\n" % (ip_from,ip_to))
	write_to_file("INFO Changing_PLC_IP_from_%s_to_%s...\n" % (ip_from,ip_to));

	if plc_type == "":
		print("Could not change the settings: target device not specified!")
		write_to_file("FAILED Could_not_change_the_settings:_target_device_not_specified!")
		return
	if login == "" or password == "":
		print("Could not change the settings: missing login credentials!")
		write_to_file("FAILED Could_not_change_the_settings:_missing_login_credentials!")
		return
	if ip_from == "" or ip_to == "":
		print("Could not change the settings: IP addresses incomplete!")
		write_to_file("FAILED Could_not_change_the_settings:_IP_addresses_incomplete!")
		return

	change_success = False

	if plc_type == PLC_WAGO_880:
		change_success = change_plc_setting_880(ip_from, ip_to, login, password)
	if plc_type == PLC_WAGO_8202:
		change_success = change_plc_setting_8202(ip_from, ip_to, login, password)

	if change_success:
		check_new_plc_settings(ip_to, plc_type)

	return


def change_plc_setting_880(ip_from, ip_to, user, password):
	"""WAGO 750-880 specific HTTP request for IP address modification"""
	form = {'IP-ADDRESS': ip_to,
			'SUBNET-MASK': '255.255.255.0',
			'GATEWAY': '0.0.0.0',
			'HOSTNAME': '0030DE0CC4A3',
			'DOMAIN': '',
			'DNS-SERV1': '0.0.0.0',
			'DNS-SERV2': '0.0.0.0',
			'SW-IP-ADDRESS': '192.168.1',
			'TIME-SERV': '0.0.0.0',
			'UPDATETIME': '3600',
			'IPFRAGTTL': '60',
			'SUBMIT': 'SUBMIT'}

	headers = {'Referer': 'http://%s/%s' % (ip_from, PLC_WAGO_880_CONFIG_URL)}
	# Changing IP address
	try:
		page = requests.post(url="http://%s/SETBOOTCNF" % ip_from, data=form, headers=headers, auth=(user, password), timeout=TIMEOUT)

		if page.status_code != 200:
			print("\nError while changing IP address... Status code: " + str(page.status_code))
			write_to_file("FAILED Error_while_changing_IP_address..._Status code:" + str(page.status_code) +"\n")
			return False

	except requests.exceptions.RequestException as err:
		print(str(err))
		write_to_file("FAILED " + str(err) +"\n")
		return False

	headers = {'Referer': 'http://%s/%s' % (ip_from, PLC_WAGO_880_RESET_URL)}
	form = {'DO_SWRST': "Software Reset"}

	# Resetting the device
	try:
		page = requests.post(url="http://%s/DOCPLFUNC" % ip_from, data=form, headers=headers, auth=(user, password),
							 timeout=TIMEOUT)
		if page:
			print("\nError while changing IP address... http status code: " + page.status_code)
			write_to_file("FAILED Error_while_changing_IP_address..._http_status_code:" + page.status_code+"\n")
	except requests.exceptions.Timeout:
		return True

	return False


def change_plc_setting_8202(ip_from, ip_to, user, password):
	"""WAGO 750-8202 specific HTTP request for IP address modification"""
	cmd = (
				  "curl --user %s:%s -d \"[{\"\'\"name\":\"config_interfaces\",\"sudo\":true,\"parameter\":[\"interface=eth0\",\"state=enabled\",\"config-type=static\", \"ip-address=%s\",\"subnet-mask=255.255.255.0\"]}]\' -H \"Content-Type: application/x-www-form-urlencoded\" -X POST http://%s" + PLC_WAGO_8202_CONFIG_URL) % (
			  user, password, ip_to, ip_from)

	curl_process = subprocess.Popen([cmd], stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)

	poll_obj = select.poll()
	poll_obj.register(	curl_process.stdout, select.POLLIN)

	counter = 0
	# Custom timeout logic implementation for subprocess.Popen - waiting TIMEOUT sec for the response
	while counter != TIMEOUT:
		poll_result = poll_obj.poll(1000)  # Waiting 1 sec for process termination
		if poll_result:
			output = curl_process.stdout.readline()
			output_line = output.strip()
			data = json.loads(output_line.strip("[]"))
			error_msg = data['errorText']
			print("\nError while changing IP address: " + error_msg)
			return False
		counter = counter + 1

	return True


def check_new_plc_settings(ip_to, plc_type):
	"""Checking connectivity after settings change"""
	print("Checking PLC connectivity...")
	write_to_file("INFO Checking_PLC_connectivity...\n")

	if plc_type == "880":
		url = PLC_WAGO_880_INFO_URL
	if plc_type == PLC_WAGO_8202:
		url = PLC_WAGO_8202_INFO_URL

	session = requests.Session()
	adapter = requests.adapters.HTTPAdapter(max_retries=MAX_RETRIES)
	session.mount("http://%s" % ip_to, adapter)

	try:
		page = session.get("http://%s/%s" % (ip_to, url), timeout=TIMEOUT)
	except requests.exceptions.RequestException as err:
		print("\nPLC did not reply at IP: %s port: 80 (http)\n" % ip_to + str(err))
		write_to_file("FAILED PLC_did_not_reply_at_IP:_%s_port:_80_(http)" % ip_to + str(err)+"\n")
		return

	if page.status_code == 200:
		print("\nSuccess! PLC replies at IP: %s port: 80 (http) with status code: %s" % (ip_to, page.status_code))
		write_to_file("INFO Success!_PLC_replies_at_IP:_%s_port:_80_(http)_with_status_code:_%s\n" % (ip_to, page.status_code))
		return

def write_to_file(string):
	with open(OUTPUTFILE_TXT,"a") as myfile:
		myfile.write(string)

if __name__ == "__main__":
	try:
		args = sys.argv[1:]
		change_plc_settings(*args)
	except Exception:
		traceback.print_exc()

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="Info1/Order_number">
        <ORDER_NUMBER><xsl:apply-templates select="@*|node()" /></ORDER_NUMBER>
    </xsl:template>
    <xsl:template match="Info1/Firmware_revision">
        <FIRMWARE_REVISION><xsl:apply-templates select="@*|node()" /></FIRMWARE_REVISION>
    </xsl:template>
    <xsl:template match="Info1/IP">
        <ADDRESS><xsl:apply-templates select="@*|node()" /></ADDRESS>
    </xsl:template>
    <xsl:template match="Info1/MAC_Address">
        <ADDRESS><xsl:apply-templates select="@*|node()" /></ADDRESS>
    </xsl:template>
    <xsl:template match="Info1/PLC_Type">
        <PLC_TYPE><xsl:apply-templates select="@*|node()" /></PLC_TYPE>
    </xsl:template>
    <xsl:template match="Info4/Order_number">
        <ORDER_NUMBER><xsl:apply-templates select="@*|node()" /></ORDER_NUMBER>
    </xsl:template>
    <xsl:template match="Info4/Firmware_revision">
        <FIRMWARE_REVISION><xsl:apply-templates select="@*|node()" /></FIRMWARE_REVISION>
    </xsl:template>
    <xsl:template match="Info4/IP">
        <ADDRESS><xsl:apply-templates select="@*|node()" /></ADDRESS>
    </xsl:template>
    <xsl:template match="Info4/MAC_Address">
        <ADDRESS><xsl:apply-templates select="@*|node()" /></ADDRESS>
    </xsl:template>
    <xsl:template match="Info4/PLC_Type">
        <PLC_TYPE><xsl:apply-templates select="@*|node()" /></PLC_TYPE>
    </xsl:template>

</xsl:stylesheet>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="host/address">
        <ADDRESS><xsl:apply-templates select="@*|node()" /></ADDRESS>
    </xsl:template>
    <xsl:template match="host/ports/port">
        <PORT><xsl:apply-templates select="@*|node()" /></PORT>
    </xsl:template>
    <xsl:template match="host/ports/port/state">
        <PORT_STATE><xsl:apply-templates select="@*|node()" /></PORT_STATE>
    </xsl:template>
    <xsl:template match="host/ports/port/service">
        <PORT_SERVICE><xsl:apply-templates select="@*|node()" /></PORT_SERVICE>
    </xsl:template>
</xsl:stylesheet>
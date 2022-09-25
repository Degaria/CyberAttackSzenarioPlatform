<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="results/password">
        <PASSWORD><xsl:apply-templates select="@*|node()" /></PASSWORD>
    </xsl:template>
    <xsl:template match="results/port">
        <PORT><xsl:apply-templates select="@*|node()" /></PORT>
    </xsl:template>
    <xsl:template match="results/service">
        <PORT_SERVICE><xsl:apply-templates select="@*|node()" /></PORT_SERVICE>
    </xsl:template>
    <xsl:template match="results/host">
        <ADDRESS><xsl:apply-templates select="@*|node()" /></ADDRESS>
    </xsl:template>
    <xsl:template match="results/login">
        <USERNAME><xsl:apply-templates select="@*|node()" /></USERNAME>
    </xsl:template>

</xsl:stylesheet>
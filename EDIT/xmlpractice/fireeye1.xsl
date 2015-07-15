<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" />
<xsl:template match="/">

<html><head><title>FireEye XSLT V1</title></head><body>
<h1>title: FireEye XSLT v1 </h1>
<table boarder="1">

<xsl:for-each select="analyses_with_cncs/analyses/analysis/event/os_change/file">
<tr><td>
<xsl:value-of select="." />
</td></tr>
</xsl:for-each>
</table>

</body></html>
</xsl:template>
</xsl:stylesheet>
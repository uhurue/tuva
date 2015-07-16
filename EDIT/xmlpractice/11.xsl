<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" />
<xsl:template match="/">

<html>
<head>
<title>Wonders of the World</title>
</head>
<body>

<h1>Seven Wonders of the Ancient World</h1>
<h2>Overview</h2>
<table border="1">

<tr><th>Wonder   Name</th><th>Location</th><th>Height</th></tr>

<!-- -->
<xsl:for-each select="ancient_wonders/wonder">
<xsl:sort select="height" order="descending" data-type="number" />
<tr>

<td><a><xsl:attribute name="href">#<xsl:value-of select="name[@language='English']" /></xsl:attribute>
<strong><xsl:value-of select="name[@language='English']" /></strong></a><br/>
<xsl:apply-templates select="name[@language!='English']" /></td>

<td><xsl:value-of select="location" /></td>

<td>
<xsl:choose>
  <xsl:when test="height != 0">
    <xsl:value-of select="height" />
  </xsl:when>
  <xsl:otherwise>
    unknown
  </xsl:otherwise>
</xsl:choose>
</td>

</tr>

</xsl:for-each>

</table>

<h2>History</h2>
<xsl:for-each select="ancient_wonders/wonder">
<xsl:sort select="height" order="descending" data-type="number" />
<a><xsl:attribute name="name"><xsl:value-of select="name[@language='English']" /></xsl:attribute></a>
<xsl:value-of select="name[@language='English']" />
<xsl:apply-templates select="name[@language!='English']" />
<xsl:apply-templates select="history" />
</xsl:for-each>

</body>
</html>
</xsl:template>

<xsl:template match="name[@language!='English']">
  (<em><xsl:value-of select="." /> </em>)
</xsl:template>

<xsl:template match="history">
was built in 
<xsl:value-of select="year-built" />
<xsl:text> </xsl:text>
<xsl:value-of select="year_built/@era" />
<xsl:choose>
<xsl:when test="year_destroyed != 0">
and was destroyed by
<xsl:value-of select="how_destroyed" /> in 
<xsl:value-of select="year_destroyed" />
<xsl:text> </xsl:text>
<xsl:value-of select="year_destroyed/@era" />.
</xsl:when>
<xsl:otherwise>
is still standing today.
</xsl:otherwise>
</xsl:choose>
<br/><br/>
</xsl:template>
</xsl:stylesheet>
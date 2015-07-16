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
<xsl:for-each select=".//*">
  <!-- 바로 위 PARENT와 나를 뽑는다. -->
  <!-- <xsl:value-of select="name(..)" /> - <xsl:value-of select="name()" /><br/>  -->
  
  <!-- 이건 맨 아래줄과 같다. 현재 위치가 바뀌지 않기 때문에 -->
  <!-- <xsl:value-of select="name()" />  -->
  <!-- all ancestors -->
  <xsl:for-each select="ancestor::*">
    <xsl:value-of select="name()" /> - 
  </xsl:for-each>
  <!--  me -->
  [<xsl:value-of select="name()" />]<br/>
  
  <xsl:for-each select="//*/@*">
    <xsl:value-of select="name(@)" />,
  </xsl:for-each>
  

</xsl:for-each>
</body>
</html>
</xsl:template>


</xsl:stylesheet>
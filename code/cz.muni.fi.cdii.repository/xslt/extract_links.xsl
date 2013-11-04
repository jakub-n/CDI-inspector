<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version='2.0' 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:pom="http://maven.apache.org/POM/4.0.0"
	exclude-result-prefixes="pom">
	<xsl:output method="xml" indent="yes" />
	<xsl:template match="/pom:project/pom:repositories">
		<references size="{2 * count(./pom:repository[pom:layout='p2'])}">
<!-- 			<xsl:call-template name="process-repositories"/> -->
			<xsl:apply-templates/>
		</references>
	</xsl:template>
	<xsl:template match="pom:repository[pom:layout='p2']">
 		<xsl:variable name="url" select="./pom:url"/> 
<!-- 		<xsl:value-of select="name()"/> -->
<!-- 		<xsl:value-of select="./pom:url"/> -->
		<repository options="1" type="0" url="{$url}" uri="{$url}"/>
		<repository options="1" type="1" url="{$url}" uri="{$url}"/>
	</xsl:template>
<!-- 	<xsl:template match="/pom:project/pom:repositories/pom:repository[pom:layout='p2']"> -->
<!-- 		<xsl:value-of select="./pom:url"></xsl:value-of> -->
<!-- 		repositories -->
<!-- 	</xsl:template> -->
	<xsl:template match="text()|@*"/>
</xsl:stylesheet>
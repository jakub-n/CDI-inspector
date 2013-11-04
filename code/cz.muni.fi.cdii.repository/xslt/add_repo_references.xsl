<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version='2.0'
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="links-file-path">links document path param not set</xsl:param>
	
	<xsl:template match="/repository">
		<xsl:processing-instruction name="metadataRepository" >
			<xsl:attribute name="version">version="1.1.0"</xsl:attribute>
		</xsl:processing-instruction>
		<xsl:copy>
			<xsl:copy-of select="*" />
			<xsl:apply-templates select="document($links-file-path)/*" />
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="/references">
		<xsl:copy>
			<xsl:copy-of select="*|@*" />
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>
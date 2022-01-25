<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/">
        <drink>
            <xsl:for-each select="/cafe_order/drinks/drink">
                <name>
                    <xsl:value-of select="name"/>
                </name>
            </xsl:for-each>
        </drink>
    </xsl:template>

</xsl:stylesheet>

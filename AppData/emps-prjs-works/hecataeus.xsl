<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
  <html>
  <body>
    <h2>Hecataeus Graph</h2>
    <table border="2">
    	<tr>
    		<td valign = "top">
			    <h3>Nodes</h3>
			    <table border="1">
			      <tr bgcolor="#9acd32">
			        <th>Key</th>
			        <th>Name</th>
			        <th>Type</th>
			      </tr>
			      <xsl:for-each select="HGraph/HNodes/HNode">
			      <tr>
			        <td><xsl:value-of select="Key" /></td>
			        <td><xsl:value-of select="Name" /></td>
			        <td><xsl:value-of select="Type" /></td>
			      </tr>
			      </xsl:for-each>
			    </table>
			  </td>
		    <td valign = "top">
		    	<h3>Edges</h3>
				    <table border="1">
				      <tr bgcolor="#9acd32">
				        <th>Key</th>
				        <th>Name</th>
				        <th>Type</th>
				        <th>FromNode</th>
				        <th>ToNode</th>
				      </tr>
				      <xsl:for-each select="HGraph/HEdges/HEdge">
							<tr>
						  	<td><xsl:value-of select="Key" /></td>
				        <td><xsl:value-of select="Name" /></td>
				        <td><xsl:value-of select="Type" /></td>
				        <td><xsl:value-of select="FromNode"/></td>
				        <td><xsl:value-of select="ToNode"/></td>
						  </tr>
						  </xsl:for-each>
						</table>
				</td>
			</tr>
		</table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>


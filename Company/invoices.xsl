<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:h="http://service.example.com/invoice" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html>
<body>
  <h1>Invoice</h1>
  <table border="1">
    <tr bgcolor="#9acd32">
      <th style="text-align:left">MID</th>
      <th style="text-align:left">BName</th>
      <th style="text-align:left">BAddress</th>
      <th style="text-align:left">BPIB</th>
      <th style="text-align:left">SName</th>
      <th style="text-align:left">SAddress</th>
      <th style="text-align:left">SPIB</th>
      <th style="text-align:left">AccNum</th>
      <th style="text-align:left">InvoiceDate</th>
      <th style="text-align:left">MerchValue</th>
      <th style="text-align:left">ServValue</th>
      <th style="text-align:left">TotalVal</th>
      <th style="text-align:left">TotalDis</th>
      <th style="text-align:left">TotalTax</th>
      <th style="text-align:left">TotalDue</th>
      <th style="text-align:left">Curr</th>
      <th style="text-align:left">BillAccNum</th>
      <th style="text-align:left">ValueDate</th>
    </tr>
    <tr>
      <td><xsl:value-of select="invoice/messageId"/></td>
      <td><xsl:value-of select="invoice/buyerName"/></td>
      <td><xsl:value-of select="invoice/buyerAddress"/></td>
      <td><xsl:value-of select="invoice/buyerPIB"/></td>
      <td><xsl:value-of select="invoice/supplierName"/></td>
      <td><xsl:value-of select="invoice/supplierAddress"/></td>
      <td><xsl:value-of select="invoice/supplierPIB"/></td>
      <td><xsl:value-of select="invoice/accountNumber"/></td>
      <td><xsl:value-of select="invoice/dateOfInvoice"/></td>
      <td><xsl:value-of select="invoice/merchandiseValue"/></td>
      <td><xsl:value-of select="invoice/servicesValue"/></td>
      <td><xsl:value-of select="invoice/totalValue"/></td>
      <td><xsl:value-of select="invoice/totalDiscount"/></td>
      <td><xsl:value-of select="invoice/totalTax"/></td>
      <td><xsl:value-of select="invoice/totalDue"/></td>
      <td><xsl:value-of select="invoice/currency"/></td>
      <td><xsl:value-of select="invoice/billingAccountNumber"/></td>
      <td><xsl:value-of select="invoice/dateOfValue"/></td>
    </tr>
  </table>
  <h2>Invoice Items</h2>
  <table border="1">
    <tr bgcolor="#9acd32">
      <th style="text-align:left">Number</th>
      <th style="text-align:left">Name</th>
      <th style="text-align:left">Amount</th>
      <th style="text-align:left">Measurement Unit</th>
      <th style="text-align:left">Unit Price</th>
      <th style="text-align:left">Value</th>
      <th style="text-align:left">Discount Percent</th>
      <th style="text-align:left">Discount Total</th>
      <th style="text-align:left">Subtracted Discount</th>
      <th style="text-align:left">Tax Total</th>
      <th style="text-align:left">Kind</th>
    </tr>
    <xsl:for-each select="invoice/invoiceItem">
      <tr>
        <td><xsl:value-of select="number"/></td>
        <td><xsl:value-of select="name"/></td>
        <td><xsl:value-of select="amount"/></td>
        <td><xsl:value-of select="measurementUnit"/></td>
        <td><xsl:value-of select="unitPrice"/></td>
        <td><xsl:value-of select="value"/></td>
        <td><xsl:value-of select="discountPercent"/></td>
        <td><xsl:value-of select="discountTotal"/></td>
        <td><xsl:value-of select="subtractedDiscount"/></td>
        <td><xsl:value-of select="taxTotal"/></td>
        <td><xsl:value-of select="kind"/></td>
      </tr>
    </xsl:for-each>
  </table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>

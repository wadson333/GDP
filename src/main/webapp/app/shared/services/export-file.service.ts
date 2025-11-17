/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/restrict-plus-operands */
import { Injectable } from '@angular/core';
import * as XLSX from 'xlsx';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import dayjs from 'dayjs';
import { jsPDF } from 'jspdf';
(pdfMake as any).vfs = pdfFonts.vfs;

@Injectable({
  providedIn: 'root',
})
export class ExportService {
  exportToExcel<T>(data: T[], fileName: string): void {
    // Convert data to worksheet
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(data);

    // Create workbook and add worksheet
    const workbook: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Data');

    // Generate Excel file
    XLSX.writeFile(workbook, `${fileName}.xlsx`);
  }

  exportToPdf<T>(
    data: T[],
    title: string,
    fileName: string,
    columns: { header: string; key: string }[],
    orientation: 'portrait' | 'landscape' = 'portrait',
  ): void {
    // Prepare the data for PDF
    const docDefinition = {
      pageOrientation: orientation,
      content: [
        { text: title, style: 'header' },
        {
          table: {
            headerRows: 1,
            widths: Array(columns.length).fill('*'),
            body: [
              // Header row
              columns.map(col => col.header),
              // Data rows
              // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
              ...data.map((item): string[] => columns.map(col => String((this.getNestedValue(item, col.key) as string) ?? '-'))),
            ],
          },
        },
      ],
      styles: {
        header: {
          fontSize: 18,
          bold: true,
          margin: [0, 0, 0, 10] as [number, number, number, number],
        },
      },
      defaultStyle: {
        fontSize: 10,
      },
    };

    // Generate PDF
    pdfMake.createPdf(docDefinition).download(`${fileName}.pdf`);
  }

  // exportMedicationDetailsToPdf(medication: any, title: string): void {
  //   const doc = new jsPDF();
  //   const pageWidth = doc.internal.pageSize.width;
  //   let yPosition = 20;
  //   const leftMargin = 20;
  //   const rightMargin = pageWidth - 20;
  //   const lineHeight = 7;

  //   // Add title
  //   doc.setFontSize(16);
  //   doc.text(title, pageWidth / 2, yPosition, { align: 'center' });
  //   yPosition += lineHeight * 2;

  //   // Add medication info
  //   doc.setFontSize(12);
  //   doc.setFont('helvetica', 'bold');
  //   doc.text('General Information', leftMargin, yPosition);
  //   yPosition += lineHeight;
  //   doc.setFont('helvetica', 'normal');

  //   // Helper function for adding sections
  //   const addSection = (title: string, content: { label: string; value: any }[]) => {
  //     doc.setFont('helvetica', 'bold');
  //     doc.text(title, leftMargin, yPosition);
  //     yPosition += lineHeight;
  //     doc.setFont('helvetica', 'normal');

  //     content.forEach(item => {
  //       if (item.value) {
  //         // Check if we need a new page
  //         if (yPosition > 270) {
  //           doc.addPage();
  //           yPosition = 20;
  //         }

  //         const label = `${item.label}: `;
  //         const value = item.value.toString();

  //         // Handle long text
  //         if (value.length > 60) {
  //           doc.setFont('helvetica', 'bold');
  //           doc.text(label, leftMargin, yPosition);
  //           doc.setFont('helvetica', 'normal');
  //           const lines = doc.splitTextToSize(value, rightMargin - leftMargin - 20);
  //           doc.text(lines, leftMargin, yPosition + lineHeight);
  //           yPosition += (lines.length + 1) * lineHeight;
  //         } else {
  //           doc.setFont('helvetica', 'bold');
  //           doc.text(label, leftMargin, yPosition);
  //           doc.setFont('helvetica', 'normal');
  //           doc.text(value, leftMargin + doc.getTextWidth(label), yPosition);
  //           yPosition += lineHeight;
  //         }
  //       }
  //     });
  //     yPosition += lineHeight;
  //   };

  //   // Add sections
  //   addSection('Basic Information', [
  //     { label: 'Name', value: medication.name },
  //     { label: 'International Name', value: medication.internationalName },
  //     { label: 'ATC Code', value: medication.codeAtc },
  //     { label: 'Manufacturer', value: medication.manufacturer },
  //     { label: 'Status', value: medication.active ? 'Active' : 'Inactive' },
  //   ]);

  //   addSection('Technical Details', [
  //     { label: 'Formulation', value: medication.formulation },
  //     { label: 'Strength', value: medication.strength },
  //     { label: 'Route of Administration', value: medication.routeOfAdministration },
  //     { label: 'Marketing Auth. Number', value: medication.marketingAuthorizationNumber },
  //     { label: 'Marketing Auth. Date', value: medication.marketingAuthorizationDate ? dayjs(medication.marketingAuthorizationDate).format('DD/MM/YYYY') : null },
  //     { label: 'Expiry Date', value: medication.expiryDate ? dayjs(medication.expiryDate).format('DD/MM/YYYY') : null },
  //   ]);

  //   addSection('Medical Information', [
  //     { label: 'Description', value: medication.description },
  //     { label: 'Composition', value: medication.composition },
  //     { label: 'Contraindications', value: medication.contraindications },
  //     { label: 'Side Effects', value: medication.sideEffects },
  //     { label: 'Storage Conditions', value: medication.storageCondition },
  //   ]);

  //   // Add barcode if present
  //   if (medication.barcode) {
  //     yPosition += lineHeight;
  //     doc.setFont('helvetica', 'bold');
  //     doc.text('Barcode:', leftMargin, yPosition);
  //     // You might want to add actual barcode image here
  //     doc.setFont('helvetica', 'normal');
  //     doc.text(medication.barcode, leftMargin + doc.getTextWidth('Barcode: '), yPosition);
  //   }

  //   // Add footer with date
  //   const pageCount = doc.internal.getNumberOfPages();
  //   for (let i = 1; i <= pageCount; i++) {
  //     doc.setPage(i);
  //     doc.setFontSize(10);
  //     doc.text(
  //       `Generated on ${dayjs().format('DD/MM/YYYY HH:mm')} - Page ${i} of ${pageCount}`,
  //       pageWidth / 2,
  //       285,
  //       { align: 'center' }
  //     );
  //   }

  //   // Save the PDF
  //   doc.save(`medication-details-${medication.id}.pdf`);
  // }

  exportMedicationDetailsToPdf(medication: any, title: string): void {
    const doc = new jsPDF({
      orientation: 'portrait',
      unit: 'mm',
      format: 'a4',
    });

    const pageWidth = doc.internal.pageSize.getWidth();
    let yPosition = 20;
    const leftMargin = 20;
    const rightMargin = pageWidth - 20;
    const lineHeight = 7;

    // Add title
    doc.setFontSize(16);
    doc.text(title, pageWidth / 2, yPosition, { align: 'center' });
    yPosition += lineHeight * 2;

    // Helper function for adding sections
    const addSection = (sectionTitle: string, content: { label: string; value: any }[]): void => {
      // Check if we need a new page before starting a new section
      if (yPosition > 270) {
        doc.addPage();
        yPosition = 20;
      }

      doc.setFontSize(12);
      doc.setFont('helvetica', 'bold');
      doc.text(sectionTitle, leftMargin, yPosition);
      yPosition += lineHeight;
      doc.setFont('helvetica', 'normal');

      content.forEach(item => {
        if (item.value) {
          // Check if we need a new page
          if (yPosition > 270) {
            doc.addPage();
            yPosition = 20;
          }

          const label = `${item.label}: `;
          const value = String(item.value);

          // Handle long text
          if (value.length > 60) {
            doc.setFont('helvetica', 'bold');
            doc.text(label, leftMargin, yPosition);
            doc.setFont('helvetica', 'normal');
            const lines = doc.splitTextToSize(value, rightMargin - leftMargin - 20);
            doc.text(lines, leftMargin, yPosition + lineHeight);
            yPosition += (lines.length + 1) * lineHeight;
          } else {
            doc.setFont('helvetica', 'bold');
            doc.text(label, leftMargin, yPosition);
            doc.setFont('helvetica', 'normal');
            doc.text(value, leftMargin + doc.getTextWidth(label), yPosition);
            yPosition += lineHeight;
          }
        }
      });
      yPosition += lineHeight;
    };

    // Add sections with translations
    addSection('Basic Information  ', [
      { label: 'Name  ', value: medication.name },
      { label: 'International Name  ', value: medication.internationalName },
      { label: 'ATC Code  ', value: medication.codeAtc },
      { label: 'Manufacturer  ', value: medication.manufacturer },
      { label: 'Status  ', value: medication.active ? 'Active' : 'Inactive' },
    ]);

    addSection('Technical Details  ', [
      { label: 'Formulation  ', value: medication.formulation },
      { label: 'Strength  ', value: medication.strength },
      { label: 'Route of Administration  ', value: medication.routeOfAdministration },
      { label: 'Marketing Auth. Number  ', value: medication.marketingAuthorizationNumber },
      {
        label: 'Marketing Auth. Date  ',
        value: medication.marketingAuthorizationDate ? dayjs(medication.marketingAuthorizationDate).format('DD/MM/YYYY') : null,
      },
      { label: 'Expiry Date  ', value: medication.expiryDate ? dayjs(medication.expiryDate).format('DD/MM/YYYY') : null },
      { label: 'Unit Price  ', value: medication.unitPrice ? `${medication.unitPrice} HTG` : null },
    ]);

    addSection('Medical Information  ', [
      { label: 'Description  ', value: medication.description },
      { label: 'Composition  ', value: medication.composition },
      { label: 'Contraindications  ', value: medication.contraindications },
      { label: 'Side Effects  ', value: medication.sideEffects },
      { label: 'Storage Conditions  ', value: medication.storageCondition },
    ]);

    // Add footer with date on all pages
    const pageCount = doc.internal.pages.length - 1;
    for (let i = 1; i <= pageCount; i++) {
      doc.setPage(i);
      doc.setFontSize(10);
      doc.text(`Date ${dayjs().format('DD/MM/YYYY HH:mm')} - Page ${i} of ${pageCount}`, pageWidth / 2, 285, { align: 'center' });
    }

    // Save the PDF
    doc.save(`medication-details-${medication.id}.pdf`);
  }

  private getNestedValue(obj: any, path: string): any {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return path.split('.').reduce((value, key) => value?.[key], obj);
  }
}

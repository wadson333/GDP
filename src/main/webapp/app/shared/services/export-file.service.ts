/* eslint-disable prettier/prettier */
import { Injectable } from '@angular/core';
import * as XLSX from 'xlsx';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
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

  exportToPdf<T>(data: T[], title: string, fileName: string, columns: { header: string; key: string }[]): void {
    // Prepare the data for PDF
    const docDefinition = {
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

  private getNestedValue(obj: any, path: string): any {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return path.split('.').reduce((value, key) => value?.[key], obj);
  }
}

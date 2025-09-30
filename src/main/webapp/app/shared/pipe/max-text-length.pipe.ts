import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'maxtextlength',
})
export class MaxtextLengthPipe implements PipeTransform {
  transform(text: any, len: number): string {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return, @typescript-eslint/restrict-plus-operands
    return text.length > len ? text.toString().substring(0, len) + ' ...' : text;
  }
}

import { WebPlugin } from '@capacitor/core';

import type { SunbirdDBPlugin } from './definitions';

export class SunbirdDBWeb extends WebPlugin implements SunbirdDBPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

import { registerPlugin } from '@capacitor/core';

import type { SunbirdDBPlugin } from './definitions';

const SunbirdDB = registerPlugin<SunbirdDBPlugin>('SunbirdDB', {
  web: () => import('./web').then(m => new m.SunbirdDBWeb()),
});

export * from './definitions';
export { SunbirdDB };

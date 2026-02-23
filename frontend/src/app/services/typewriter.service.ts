import { Injectable } from '@angular/core';

export interface TypewriterLine {
  text: string;
  done: boolean;
  status?: 'ok' | 'err' | 'warn' | '';
}

@Injectable({ providedIn: 'root' })
export class TypewriterService {

  /** Animate a sequence of lines into the target array in-place. */
  async runSequence(
    lines: { text: string; status?: 'ok' | 'err' | 'warn' | '' }[],
    out: TypewriterLine[],
    charDelay = 18,
    lineDelay = 120
  ): Promise<void> {
    for (const def of lines) {
      const entry: TypewriterLine = { text: '', done: false, status: def.status ?? '' };
      out.push(entry);
      for (const ch of def.text) {
        await delay(charDelay + Math.random() * 14);
        entry.text += ch;
      }
      await delay(lineDelay);
      entry.done = true;
    }
  }
}

function delay(ms: number) {
  return new Promise(r => setTimeout(r, ms));
}

/* ————————————————————————————————————————————
   Flavor text pools — pick random lines
   ———————————————————————————————————————————— */

export const BOOT_LINES = [
  { text: 'BIOS v1.33.7 ......................... OK', status: 'ok' as const },
  { text: 'CPU CHECK ............................. OK', status: 'ok' as const },
  { text: 'MEMORY TEST (65536KB) ................. OK', status: 'ok' as const },
  { text: 'LOADING KERNEL MODULE ................. OK', status: 'ok' as const },
  { text: 'MOUNTING FILE SYSTEM .................. OK', status: 'ok' as const },
  { text: 'INITIALIZING NETWORK STACK ............ OK', status: 'ok' as const },
  { text: 'STARTING USER AUTHENTICATION .......... OK', status: 'ok' as const },
];

export const REGISTER_BOOT = [
  { text: 'ALLOCATING USER RECORD ................ OK', status: 'ok' as const },
  { text: 'GENERATING IDENTITY HASH .............. OK', status: 'ok' as const },
  { text: 'CONFIGURING NODE PERMISSIONS .......... OK', status: 'ok' as const },
  { text: 'AWAITING CREDENTIAL INPUT .............',   status: ''   as const },
];

export const FEED_QUOTES = [
  '"The terminal never lies. People do."',
  '"In the datastream, every byte has a story."',
  '"Knowledge shared is knowledge multiplied."',
  '"Signal strong. Noise minimal. Proceed."',
  '"01Blog: where humans write, machines listen."',
  '"Broadcast your signal into the void. Someone is listening."',
];

export const EXPLORE_QUOTES = [
  'SCANNING THE DATASTREAM FOR TRANSMISSIONS...',
  'QUERYING ALL NODES IN THE NETWORK...',
  'INTERCEPTING OUTGOING BROADCASTS...',
  'PARSING SIGNAL FROM NOISE...',
];

export const EMPTY_FEED_MSGS = [
  'NO TRANSMISSIONS DETECTED ON THIS FREQUENCY.',
  'YOUR FEED IS SILENT. SUBSCRIBE TO CHANGE THAT.',
  'SIGNAL LOST. FOLLOW SOME USERS TO RECEIVE DATA.',
];

export function pick<T>(arr: T[]): T {
  return arr[Math.floor(Math.random() * arr.length)];
}

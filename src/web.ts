import { WebPlugin } from '@capacitor/core';

import type { SunbirdDBPlugin } from './definitions';

export class SunbirdDBWeb extends WebPlugin implements SunbirdDBPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async init(options: { dbName: string, dbVersion: number, migrations: Array<any> }): Promise<{ callback: any }> {
    console.log("init", options);
    return {callback: ''};
  }

  async open(options: { filePath: string }): Promise<{ filePath: string }> {
    console.log('open', options);
    return options;
  }

  async close(): Promise<any> {
    console.log("close db ");
    return true;
  }

  async copyDatabase(options: {filePath: string}): Promise<string> {
    console.log('copyDatabase ', options);
    return "";
  }

  async execute(options:{query: string, useExternalDb: boolean}): Promise<any> {
    console.log("execute", options);
    return options;
  }

  async read(options: {distinct: boolean, table: string, columns: Array<any>, selection: string, selectionArgs: Array<any>, groupBy: string, having: string, orderBy: string, limit: string, useExternalDb: boolean}): Promise<any> {
    console.log("read", options);
    return options;
  }

  async insert(options: {table: string, model: object, useExternalDb: boolean}): Promise<any> {
    console.log("insert", options);
    return options;
  }

  async update(options: {table: string, whereClause: string, whereArgs: Array<any>, model: object, useExternalDb: boolean}): Promise<any> {
    console.log("update ", options);
    return options;
  }

  async delete(options: {table: string, whereClause: string, whereArgs: Array<any>, useExternalDb: boolean}): Promise<any> {
    console.log("delete ", options);
    return options;
  }

  async beginTransaction(): Promise<any> {
    console.log("beginTransaction ");
    return "bt";
  }

  async endTransaction(options: {useExternalDb: boolean, isOperationSuccessful: boolean }): Promise<any> {
    console.log("endTransaction ", options);
    return "bt";
  }
}

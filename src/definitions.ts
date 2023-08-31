export interface SunbirdDBPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  init(options: { dbName: string, dbVersion: number, migrations: Array<any>}): Promise<{ callback: string }>;
  open(options: { filePath: string }): Promise<{ filePath: string }>;
  close(): Promise<any>;
  copyDatabase(options: {filePath: string}): Promise<string>;
  execute(options:{query: string, useExternalDb: boolean}): Promise<any>;
  read(options: {distinct: boolean, table: string, columns: Array<any>, selection: string, selectionArgs: Array<any>, groupBy: string, having: string, orderBy: string, limit: string, useExternalDb: boolean}): Promise<any>;
  insert(options: {table: string, model: object, useExternalDb: boolean}): Promise<any>;
  update(options: {table: string, whereClause: string, whereArgs: Array<any>, model: object, useExternalDb: boolean}): Promise<any>;
  delete(options: {table: string, whereClause: string, whereArgs: Array<any>, useExternalDb: boolean}): Promise<any>;
  beginTransaction(): Promise<any>;
  endTransaction(options: {useExternalDb: boolean, isOperationSuccessful: boolean }): Promise<any>;
}

export interface SunbirdDBPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

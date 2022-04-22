export interface IPaintingOrSculpture {
  id?: string;
  materials?: string | null;
  techniques?: string | null;
  size?: string | null;
  place?: string | null;
}

export const defaultValue: Readonly<IPaintingOrSculpture> = {};

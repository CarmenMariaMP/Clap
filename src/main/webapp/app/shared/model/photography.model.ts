export interface IPhotography {
  id?: string;
  camera?: string | null;
  techniques?: string | null;
  size?: string | null;
  place?: string | null;
}

export const defaultValue: Readonly<IPhotography> = {};

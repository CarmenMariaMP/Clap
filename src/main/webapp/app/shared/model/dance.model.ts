import { DanceGenreType } from 'app/shared/model/enumerations/dance-genre-type.model';

export interface IDance {
  id?: string;
  music?: string | null;
  genres?: DanceGenreType | null;
}

export const defaultValue: Readonly<IDance> = {};

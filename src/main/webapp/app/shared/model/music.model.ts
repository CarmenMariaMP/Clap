import { MusicGenreType } from 'app/shared/model/enumerations/music-genre-type.model';

export interface IMusic {
  id?: string;
  genres?: MusicGenreType | null;
}

export const defaultValue: Readonly<IMusic> = {};

import { CinemaGenreType } from 'app/shared/model/enumerations/cinema-genre-type.model';

export interface ICinema {
  id?: string;
  genres?: CinemaGenreType | null;
}

export const defaultValue: Readonly<ICinema> = {};

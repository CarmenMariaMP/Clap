import dayjs from 'dayjs';

export interface IComment {
  id?: string;
  text?: string | null;
  date?: string | null;
}

export const defaultValue: Readonly<IComment> = {};

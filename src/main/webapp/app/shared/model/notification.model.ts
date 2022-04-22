import dayjs from 'dayjs';

export interface INotification {
  id?: string;
  text?: string | null;
  emisionDate?: string | null;
  readDate?: string | null;
}

export const defaultValue: Readonly<INotification> = {};

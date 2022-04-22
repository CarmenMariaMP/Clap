import dayjs from 'dayjs';
import { IArtisticContent } from 'app/shared/model/artistic-content.model';

export interface IProject {
  id?: string;
  creationDate?: string | null;
  artisticContents?: IArtisticContent[] | null;
}

export const defaultValue: Readonly<IProject> = {};

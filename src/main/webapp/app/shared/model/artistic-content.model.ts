import dayjs from 'dayjs';
import { ITag } from 'app/shared/model/tag.model';
import { IProject } from 'app/shared/model/project.model';

export interface IArtisticContent {
  id?: string;
  title?: string | null;
  description?: string | null;
  contentUrl?: string | null;
  uploadDate?: string | null;
  viewCount?: number | null;
  tags?: ITag[] | null;
  projects?: IProject[] | null;
}

export const defaultValue: Readonly<IArtisticContent> = {};

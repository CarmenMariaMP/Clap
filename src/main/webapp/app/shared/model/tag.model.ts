import { IArtisticContent } from 'app/shared/model/artistic-content.model';

export interface ITag {
  id?: string;
  name?: string | null;
  artisticContents?: IArtisticContent[] | null;
}

export const defaultValue: Readonly<ITag> = {};

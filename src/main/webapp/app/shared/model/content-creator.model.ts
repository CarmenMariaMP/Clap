import { IPrivacyRequest } from 'app/shared/model/privacy-request.model';

export interface IContentCreator {
  id?: string;
  fullName?: string | null;
  country?: string | null;
  city?: string | null;
  privacyRequests?: IPrivacyRequest[] | null;
}

export const defaultValue: Readonly<IContentCreator> = {};

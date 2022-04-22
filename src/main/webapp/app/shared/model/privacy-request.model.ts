import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';
import { IContentCreator } from 'app/shared/model/content-creator.model';
import { RequestStateType } from 'app/shared/model/enumerations/request-state-type.model';

export interface IPrivacyRequest {
  id?: string;
  requestState?: RequestStateType | null;
  requestDate?: string | null;
  company?: ICompany | null;
  contentCreatto?: IContentCreator | null;
}

export const defaultValue: Readonly<IPrivacyRequest> = {};

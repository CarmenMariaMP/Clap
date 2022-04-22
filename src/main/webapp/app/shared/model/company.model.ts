import { IPrivacyRequest } from 'app/shared/model/privacy-request.model';

export interface ICompany {
  id?: string;
  companyName?: string | null;
  taxIdNumber?: string | null;
  officeAddress?: string | null;
  privacyRequests?: IPrivacyRequest[] | null;
}

export const defaultValue: Readonly<ICompany> = {};

import { IUser } from 'app/shared/model/user.model';

export interface INotificationConfiguration {
  id?: string;
  byComments?: boolean | null;
  byLikes?: boolean | null;
  bySavings?: boolean | null;
  bySubscriptions?: boolean | null;
  byPrivacyRequests?: boolean | null;
  byPrivacyRequestsStatus?: boolean | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<INotificationConfiguration> = {
  byComments: false,
  byLikes: false,
  bySavings: false,
  bySubscriptions: false,
  byPrivacyRequests: false,
  byPrivacyRequestsStatus: false,
};

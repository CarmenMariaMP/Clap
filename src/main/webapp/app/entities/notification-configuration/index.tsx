import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import NotificationConfiguration from './notification-configuration';
import NotificationConfigurationDetail from './notification-configuration-detail';
import NotificationConfigurationUpdate from './notification-configuration-update';
import NotificationConfigurationDeleteDialog from './notification-configuration-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NotificationConfigurationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NotificationConfigurationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NotificationConfigurationDetail} />
      <ErrorBoundaryRoute path={match.url} component={NotificationConfiguration} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={NotificationConfigurationDeleteDialog} />
  </>
);

export default Routes;

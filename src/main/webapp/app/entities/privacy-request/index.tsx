import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PrivacyRequest from './privacy-request';
import PrivacyRequestDetail from './privacy-request-detail';
import PrivacyRequestUpdate from './privacy-request-update';
import PrivacyRequestDeleteDialog from './privacy-request-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PrivacyRequestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PrivacyRequestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PrivacyRequestDetail} />
      <ErrorBoundaryRoute path={match.url} component={PrivacyRequest} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PrivacyRequestDeleteDialog} />
  </>
);

export default Routes;

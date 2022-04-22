import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import General from './general';
import GeneralDetail from './general-detail';
import GeneralUpdate from './general-update';
import GeneralDeleteDialog from './general-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GeneralUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GeneralUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GeneralDetail} />
      <ErrorBoundaryRoute path={match.url} component={General} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GeneralDeleteDialog} />
  </>
);

export default Routes;

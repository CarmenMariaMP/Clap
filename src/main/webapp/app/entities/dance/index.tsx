import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Dance from './dance';
import DanceDetail from './dance-detail';
import DanceUpdate from './dance-update';
import DanceDeleteDialog from './dance-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DanceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DanceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DanceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Dance} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DanceDeleteDialog} />
  </>
);

export default Routes;

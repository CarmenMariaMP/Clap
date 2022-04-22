import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cinema from './cinema';
import CinemaDetail from './cinema-detail';
import CinemaUpdate from './cinema-update';
import CinemaDeleteDialog from './cinema-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CinemaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CinemaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CinemaDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cinema} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CinemaDeleteDialog} />
  </>
);

export default Routes;

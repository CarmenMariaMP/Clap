import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Music from './music';
import MusicDetail from './music-detail';
import MusicUpdate from './music-update';
import MusicDeleteDialog from './music-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MusicUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MusicUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MusicDetail} />
      <ErrorBoundaryRoute path={match.url} component={Music} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MusicDeleteDialog} />
  </>
);

export default Routes;

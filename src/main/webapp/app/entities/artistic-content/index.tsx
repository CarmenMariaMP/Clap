import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ArtisticContent from './artistic-content';
import ArtisticContentDetail from './artistic-content-detail';
import ArtisticContentUpdate from './artistic-content-update';
import ArtisticContentDeleteDialog from './artistic-content-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ArtisticContentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ArtisticContentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ArtisticContentDetail} />
      <ErrorBoundaryRoute path={match.url} component={ArtisticContent} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ArtisticContentDeleteDialog} />
  </>
);

export default Routes;

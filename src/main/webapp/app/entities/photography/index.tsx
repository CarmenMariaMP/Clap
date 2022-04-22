import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Photography from './photography';
import PhotographyDetail from './photography-detail';
import PhotographyUpdate from './photography-update';
import PhotographyDeleteDialog from './photography-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PhotographyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PhotographyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PhotographyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Photography} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PhotographyDeleteDialog} />
  </>
);

export default Routes;

import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContentCreator from './content-creator';
import ContentCreatorDetail from './content-creator-detail';
import ContentCreatorUpdate from './content-creator-update';
import ContentCreatorDeleteDialog from './content-creator-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContentCreatorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContentCreatorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContentCreatorDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContentCreator} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ContentCreatorDeleteDialog} />
  </>
);

export default Routes;

import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PaintingOrSculpture from './painting-or-sculpture';
import PaintingOrSculptureDetail from './painting-or-sculpture-detail';
import PaintingOrSculptureUpdate from './painting-or-sculpture-update';
import PaintingOrSculptureDeleteDialog from './painting-or-sculpture-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaintingOrSculptureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaintingOrSculptureUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaintingOrSculptureDetail} />
      <ErrorBoundaryRoute path={match.url} component={PaintingOrSculpture} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PaintingOrSculptureDeleteDialog} />
  </>
);

export default Routes;

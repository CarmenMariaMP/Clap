import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContentCreator from './content-creator';
import Company from './company';
import ArtisticContent from './artistic-content';
import General from './general';
import Photography from './photography';
import PaintingOrSculpture from './painting-or-sculpture';
import Music from './music';
import Dance from './dance';
import Cinema from './cinema';
import Tag from './tag';
import Comment from './comment';
import Notification from './notification';
import NotificationConfiguration from './notification-configuration';
import PrivacyRequest from './privacy-request';
import Project from './project';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}content-creator`} component={ContentCreator} />
        <ErrorBoundaryRoute path={`${match.url}company`} component={Company} />
        <ErrorBoundaryRoute path={`${match.url}artistic-content`} component={ArtisticContent} />
        <ErrorBoundaryRoute path={`${match.url}general`} component={General} />
        <ErrorBoundaryRoute path={`${match.url}photography`} component={Photography} />
        <ErrorBoundaryRoute path={`${match.url}painting-or-sculpture`} component={PaintingOrSculpture} />
        <ErrorBoundaryRoute path={`${match.url}music`} component={Music} />
        <ErrorBoundaryRoute path={`${match.url}dance`} component={Dance} />
        <ErrorBoundaryRoute path={`${match.url}cinema`} component={Cinema} />
        <ErrorBoundaryRoute path={`${match.url}tag`} component={Tag} />
        <ErrorBoundaryRoute path={`${match.url}comment`} component={Comment} />
        <ErrorBoundaryRoute path={`${match.url}notification`} component={Notification} />
        <ErrorBoundaryRoute path={`${match.url}notification-configuration`} component={NotificationConfiguration} />
        <ErrorBoundaryRoute path={`${match.url}privacy-request`} component={PrivacyRequest} />
        <ErrorBoundaryRoute path={`${match.url}project`} component={Project} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};

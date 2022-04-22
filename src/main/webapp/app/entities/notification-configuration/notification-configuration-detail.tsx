import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './notification-configuration.reducer';

export const NotificationConfigurationDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const notificationConfigurationEntity = useAppSelector(state => state.notificationConfiguration.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notificationConfigurationDetailsHeading">
          <Translate contentKey="clapApplicationApp.notificationConfiguration.detail.title">NotificationConfiguration</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{notificationConfigurationEntity.id}</dd>
          <dt>
            <span id="byComments">
              <Translate contentKey="clapApplicationApp.notificationConfiguration.byComments">By Comments</Translate>
            </span>
          </dt>
          <dd>{notificationConfigurationEntity.byComments ? 'true' : 'false'}</dd>
          <dt>
            <span id="byLikes">
              <Translate contentKey="clapApplicationApp.notificationConfiguration.byLikes">By Likes</Translate>
            </span>
          </dt>
          <dd>{notificationConfigurationEntity.byLikes ? 'true' : 'false'}</dd>
          <dt>
            <span id="bySavings">
              <Translate contentKey="clapApplicationApp.notificationConfiguration.bySavings">By Savings</Translate>
            </span>
          </dt>
          <dd>{notificationConfigurationEntity.bySavings ? 'true' : 'false'}</dd>
          <dt>
            <span id="bySubscriptions">
              <Translate contentKey="clapApplicationApp.notificationConfiguration.bySubscriptions">By Subscriptions</Translate>
            </span>
          </dt>
          <dd>{notificationConfigurationEntity.bySubscriptions ? 'true' : 'false'}</dd>
          <dt>
            <span id="byPrivacyRequests">
              <Translate contentKey="clapApplicationApp.notificationConfiguration.byPrivacyRequests">By Privacy Requests</Translate>
            </span>
          </dt>
          <dd>{notificationConfigurationEntity.byPrivacyRequests ? 'true' : 'false'}</dd>
          <dt>
            <span id="byPrivacyRequestsStatus">
              <Translate contentKey="clapApplicationApp.notificationConfiguration.byPrivacyRequestsStatus">
                By Privacy Requests Status
              </Translate>
            </span>
          </dt>
          <dd>{notificationConfigurationEntity.byPrivacyRequestsStatus ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="clapApplicationApp.notificationConfiguration.user">User</Translate>
          </dt>
          <dd>{notificationConfigurationEntity.user ? notificationConfigurationEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/notification-configuration" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notification-configuration/${notificationConfigurationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotificationConfigurationDetail;

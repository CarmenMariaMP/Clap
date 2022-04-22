import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { INotificationConfiguration } from 'app/shared/model/notification-configuration.model';
import { getEntities } from './notification-configuration.reducer';

export const NotificationConfiguration = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const notificationConfigurationList = useAppSelector(state => state.notificationConfiguration.entities);
  const loading = useAppSelector(state => state.notificationConfiguration.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="notification-configuration-heading" data-cy="NotificationConfigurationHeading">
        <Translate contentKey="clapApplicationApp.notificationConfiguration.home.title">Notification Configurations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="clapApplicationApp.notificationConfiguration.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/notification-configuration/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="clapApplicationApp.notificationConfiguration.home.createLabel">
              Create new Notification Configuration
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {notificationConfigurationList && notificationConfigurationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="clapApplicationApp.notificationConfiguration.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.notificationConfiguration.byComments">By Comments</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.notificationConfiguration.byLikes">By Likes</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.notificationConfiguration.bySavings">By Savings</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.notificationConfiguration.bySubscriptions">By Subscriptions</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.notificationConfiguration.byPrivacyRequests">By Privacy Requests</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.notificationConfiguration.byPrivacyRequestsStatus">
                    By Privacy Requests Status
                  </Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.notificationConfiguration.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {notificationConfigurationList.map((notificationConfiguration, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/notification-configuration/${notificationConfiguration.id}`} color="link" size="sm">
                      {notificationConfiguration.id}
                    </Button>
                  </td>
                  <td>{notificationConfiguration.byComments ? 'true' : 'false'}</td>
                  <td>{notificationConfiguration.byLikes ? 'true' : 'false'}</td>
                  <td>{notificationConfiguration.bySavings ? 'true' : 'false'}</td>
                  <td>{notificationConfiguration.bySubscriptions ? 'true' : 'false'}</td>
                  <td>{notificationConfiguration.byPrivacyRequests ? 'true' : 'false'}</td>
                  <td>{notificationConfiguration.byPrivacyRequestsStatus ? 'true' : 'false'}</td>
                  <td>{notificationConfiguration.user ? notificationConfiguration.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/notification-configuration/${notificationConfiguration.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/notification-configuration/${notificationConfiguration.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/notification-configuration/${notificationConfiguration.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="clapApplicationApp.notificationConfiguration.home.notFound">
                No Notification Configurations found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default NotificationConfiguration;

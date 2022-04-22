import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPrivacyRequest } from 'app/shared/model/privacy-request.model';
import { getEntities } from './privacy-request.reducer';

export const PrivacyRequest = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const privacyRequestList = useAppSelector(state => state.privacyRequest.entities);
  const loading = useAppSelector(state => state.privacyRequest.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="privacy-request-heading" data-cy="PrivacyRequestHeading">
        <Translate contentKey="clapApplicationApp.privacyRequest.home.title">Privacy Requests</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="clapApplicationApp.privacyRequest.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/privacy-request/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="clapApplicationApp.privacyRequest.home.createLabel">Create new Privacy Request</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {privacyRequestList && privacyRequestList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="clapApplicationApp.privacyRequest.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.privacyRequest.requestState">Request State</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.privacyRequest.requestDate">Request Date</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.privacyRequest.company">Company</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.privacyRequest.contentCreatto">Content Creatto</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {privacyRequestList.map((privacyRequest, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/privacy-request/${privacyRequest.id}`} color="link" size="sm">
                      {privacyRequest.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`clapApplicationApp.RequestStateType.${privacyRequest.requestState}`} />
                  </td>
                  <td>
                    {privacyRequest.requestDate ? (
                      <TextFormat type="date" value={privacyRequest.requestDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {privacyRequest.company ? <Link to={`/company/${privacyRequest.company.id}`}>{privacyRequest.company.id}</Link> : ''}
                  </td>
                  <td>
                    {privacyRequest.contentCreatto ? (
                      <Link to={`/content-creator/${privacyRequest.contentCreatto.id}`}>{privacyRequest.contentCreatto.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/privacy-request/${privacyRequest.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/privacy-request/${privacyRequest.id}/edit`}
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
                        to={`/privacy-request/${privacyRequest.id}/delete`}
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
              <Translate contentKey="clapApplicationApp.privacyRequest.home.notFound">No Privacy Requests found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PrivacyRequest;

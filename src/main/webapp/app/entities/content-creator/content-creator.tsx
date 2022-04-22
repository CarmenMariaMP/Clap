import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IContentCreator } from 'app/shared/model/content-creator.model';
import { getEntities } from './content-creator.reducer';

export const ContentCreator = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const contentCreatorList = useAppSelector(state => state.contentCreator.entities);
  const loading = useAppSelector(state => state.contentCreator.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="content-creator-heading" data-cy="ContentCreatorHeading">
        <Translate contentKey="clapApplicationApp.contentCreator.home.title">Content Creators</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="clapApplicationApp.contentCreator.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/content-creator/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="clapApplicationApp.contentCreator.home.createLabel">Create new Content Creator</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {contentCreatorList && contentCreatorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="clapApplicationApp.contentCreator.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.contentCreator.fullName">Full Name</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.contentCreator.country">Country</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.contentCreator.city">City</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contentCreatorList.map((contentCreator, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/content-creator/${contentCreator.id}`} color="link" size="sm">
                      {contentCreator.id}
                    </Button>
                  </td>
                  <td>{contentCreator.fullName}</td>
                  <td>{contentCreator.country}</td>
                  <td>{contentCreator.city}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/content-creator/${contentCreator.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/content-creator/${contentCreator.id}/edit`}
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
                        to={`/content-creator/${contentCreator.id}/delete`}
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
              <Translate contentKey="clapApplicationApp.contentCreator.home.notFound">No Content Creators found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ContentCreator;

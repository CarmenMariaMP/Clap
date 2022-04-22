import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPhotography } from 'app/shared/model/photography.model';
import { getEntities } from './photography.reducer';

export const Photography = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const photographyList = useAppSelector(state => state.photography.entities);
  const loading = useAppSelector(state => state.photography.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="photography-heading" data-cy="PhotographyHeading">
        <Translate contentKey="clapApplicationApp.photography.home.title">Photographies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="clapApplicationApp.photography.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/photography/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="clapApplicationApp.photography.home.createLabel">Create new Photography</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {photographyList && photographyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="clapApplicationApp.photography.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.photography.camera">Camera</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.photography.techniques">Techniques</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.photography.size">Size</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.photography.place">Place</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {photographyList.map((photography, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/photography/${photography.id}`} color="link" size="sm">
                      {photography.id}
                    </Button>
                  </td>
                  <td>{photography.camera}</td>
                  <td>{photography.techniques}</td>
                  <td>{photography.size}</td>
                  <td>{photography.place}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/photography/${photography.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/photography/${photography.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/photography/${photography.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="clapApplicationApp.photography.home.notFound">No Photographies found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Photography;

import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPaintingOrSculpture } from 'app/shared/model/painting-or-sculpture.model';
import { getEntities } from './painting-or-sculpture.reducer';

export const PaintingOrSculpture = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const paintingOrSculptureList = useAppSelector(state => state.paintingOrSculpture.entities);
  const loading = useAppSelector(state => state.paintingOrSculpture.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="painting-or-sculpture-heading" data-cy="PaintingOrSculptureHeading">
        <Translate contentKey="clapApplicationApp.paintingOrSculpture.home.title">Painting Or Sculptures</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="clapApplicationApp.paintingOrSculpture.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/painting-or-sculpture/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="clapApplicationApp.paintingOrSculpture.home.createLabel">Create new Painting Or Sculpture</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {paintingOrSculptureList && paintingOrSculptureList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="clapApplicationApp.paintingOrSculpture.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.paintingOrSculpture.materials">Materials</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.paintingOrSculpture.techniques">Techniques</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.paintingOrSculpture.size">Size</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.paintingOrSculpture.place">Place</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {paintingOrSculptureList.map((paintingOrSculpture, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/painting-or-sculpture/${paintingOrSculpture.id}`} color="link" size="sm">
                      {paintingOrSculpture.id}
                    </Button>
                  </td>
                  <td>{paintingOrSculpture.materials}</td>
                  <td>{paintingOrSculpture.techniques}</td>
                  <td>{paintingOrSculpture.size}</td>
                  <td>{paintingOrSculpture.place}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/painting-or-sculpture/${paintingOrSculpture.id}`}
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
                        to={`/painting-or-sculpture/${paintingOrSculpture.id}/edit`}
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
                        to={`/painting-or-sculpture/${paintingOrSculpture.id}/delete`}
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
              <Translate contentKey="clapApplicationApp.paintingOrSculpture.home.notFound">No Painting Or Sculptures found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PaintingOrSculpture;

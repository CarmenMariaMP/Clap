import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICinema } from 'app/shared/model/cinema.model';
import { getEntities } from './cinema.reducer';

export const Cinema = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const cinemaList = useAppSelector(state => state.cinema.entities);
  const loading = useAppSelector(state => state.cinema.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="cinema-heading" data-cy="CinemaHeading">
        <Translate contentKey="clapApplicationApp.cinema.home.title">Cinemas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="clapApplicationApp.cinema.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cinema/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="clapApplicationApp.cinema.home.createLabel">Create new Cinema</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cinemaList && cinemaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="clapApplicationApp.cinema.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.cinema.genres">Genres</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cinemaList.map((cinema, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/cinema/${cinema.id}`} color="link" size="sm">
                      {cinema.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`clapApplicationApp.CinemaGenreType.${cinema.genres}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/cinema/${cinema.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/cinema/${cinema.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/cinema/${cinema.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="clapApplicationApp.cinema.home.notFound">No Cinemas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Cinema;

import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArtisticContent } from 'app/shared/model/artistic-content.model';
import { getEntities } from './artistic-content.reducer';

export const ArtisticContent = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const artisticContentList = useAppSelector(state => state.artisticContent.entities);
  const loading = useAppSelector(state => state.artisticContent.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="artistic-content-heading" data-cy="ArtisticContentHeading">
        <Translate contentKey="clapApplicationApp.artisticContent.home.title">Artistic Contents</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="clapApplicationApp.artisticContent.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/artistic-content/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="clapApplicationApp.artisticContent.home.createLabel">Create new Artistic Content</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {artisticContentList && artisticContentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="clapApplicationApp.artisticContent.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.artisticContent.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.artisticContent.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.artisticContent.contentUrl">Content Url</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.artisticContent.uploadDate">Upload Date</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.artisticContent.viewCount">View Count</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.artisticContent.tag">Tag</Translate>
                </th>
                <th>
                  <Translate contentKey="clapApplicationApp.artisticContent.project">Project</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {artisticContentList.map((artisticContent, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/artistic-content/${artisticContent.id}`} color="link" size="sm">
                      {artisticContent.id}
                    </Button>
                  </td>
                  <td>{artisticContent.title}</td>
                  <td>{artisticContent.description}</td>
                  <td>{artisticContent.contentUrl}</td>
                  <td>
                    {artisticContent.uploadDate ? (
                      <TextFormat type="date" value={artisticContent.uploadDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{artisticContent.viewCount}</td>
                  <td>
                    {artisticContent.tags
                      ? artisticContent.tags.map((val, j) => (
                          <span key={j}>
                            <Link to={`/tag/${val.id}`}>{val.id}</Link>
                            {j === artisticContent.tags.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {artisticContent.projects
                      ? artisticContent.projects.map((val, j) => (
                          <span key={j}>
                            <Link to={`/project/${val.id}`}>{val.id}</Link>
                            {j === artisticContent.projects.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/artistic-content/${artisticContent.id}`}
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
                        to={`/artistic-content/${artisticContent.id}/edit`}
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
                        to={`/artistic-content/${artisticContent.id}/delete`}
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
              <Translate contentKey="clapApplicationApp.artisticContent.home.notFound">No Artistic Contents found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ArtisticContent;

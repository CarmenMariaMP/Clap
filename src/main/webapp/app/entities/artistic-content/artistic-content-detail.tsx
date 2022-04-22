import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './artistic-content.reducer';

export const ArtisticContentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const artisticContentEntity = useAppSelector(state => state.artisticContent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="artisticContentDetailsHeading">
          <Translate contentKey="clapApplicationApp.artisticContent.detail.title">ArtisticContent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{artisticContentEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="clapApplicationApp.artisticContent.title">Title</Translate>
            </span>
          </dt>
          <dd>{artisticContentEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="clapApplicationApp.artisticContent.description">Description</Translate>
            </span>
          </dt>
          <dd>{artisticContentEntity.description}</dd>
          <dt>
            <span id="contentUrl">
              <Translate contentKey="clapApplicationApp.artisticContent.contentUrl">Content Url</Translate>
            </span>
          </dt>
          <dd>{artisticContentEntity.contentUrl}</dd>
          <dt>
            <span id="uploadDate">
              <Translate contentKey="clapApplicationApp.artisticContent.uploadDate">Upload Date</Translate>
            </span>
          </dt>
          <dd>
            {artisticContentEntity.uploadDate ? (
              <TextFormat value={artisticContentEntity.uploadDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="viewCount">
              <Translate contentKey="clapApplicationApp.artisticContent.viewCount">View Count</Translate>
            </span>
          </dt>
          <dd>{artisticContentEntity.viewCount}</dd>
          <dt>
            <Translate contentKey="clapApplicationApp.artisticContent.tag">Tag</Translate>
          </dt>
          <dd>
            {artisticContentEntity.tags
              ? artisticContentEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {artisticContentEntity.tags && i === artisticContentEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="clapApplicationApp.artisticContent.project">Project</Translate>
          </dt>
          <dd>
            {artisticContentEntity.projects
              ? artisticContentEntity.projects.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {artisticContentEntity.projects && i === artisticContentEntity.projects.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/artistic-content" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/artistic-content/${artisticContentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArtisticContentDetail;

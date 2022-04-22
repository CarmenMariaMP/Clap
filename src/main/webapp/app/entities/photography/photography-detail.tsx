import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './photography.reducer';

export const PhotographyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const photographyEntity = useAppSelector(state => state.photography.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="photographyDetailsHeading">
          <Translate contentKey="clapApplicationApp.photography.detail.title">Photography</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{photographyEntity.id}</dd>
          <dt>
            <span id="camera">
              <Translate contentKey="clapApplicationApp.photography.camera">Camera</Translate>
            </span>
          </dt>
          <dd>{photographyEntity.camera}</dd>
          <dt>
            <span id="techniques">
              <Translate contentKey="clapApplicationApp.photography.techniques">Techniques</Translate>
            </span>
          </dt>
          <dd>{photographyEntity.techniques}</dd>
          <dt>
            <span id="size">
              <Translate contentKey="clapApplicationApp.photography.size">Size</Translate>
            </span>
          </dt>
          <dd>{photographyEntity.size}</dd>
          <dt>
            <span id="place">
              <Translate contentKey="clapApplicationApp.photography.place">Place</Translate>
            </span>
          </dt>
          <dd>{photographyEntity.place}</dd>
        </dl>
        <Button tag={Link} to="/photography" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/photography/${photographyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhotographyDetail;

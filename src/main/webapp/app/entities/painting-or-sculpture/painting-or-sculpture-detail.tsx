import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './painting-or-sculpture.reducer';

export const PaintingOrSculptureDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const paintingOrSculptureEntity = useAppSelector(state => state.paintingOrSculpture.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paintingOrSculptureDetailsHeading">
          <Translate contentKey="clapApplicationApp.paintingOrSculpture.detail.title">PaintingOrSculpture</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paintingOrSculptureEntity.id}</dd>
          <dt>
            <span id="materials">
              <Translate contentKey="clapApplicationApp.paintingOrSculpture.materials">Materials</Translate>
            </span>
          </dt>
          <dd>{paintingOrSculptureEntity.materials}</dd>
          <dt>
            <span id="techniques">
              <Translate contentKey="clapApplicationApp.paintingOrSculpture.techniques">Techniques</Translate>
            </span>
          </dt>
          <dd>{paintingOrSculptureEntity.techniques}</dd>
          <dt>
            <span id="size">
              <Translate contentKey="clapApplicationApp.paintingOrSculpture.size">Size</Translate>
            </span>
          </dt>
          <dd>{paintingOrSculptureEntity.size}</dd>
          <dt>
            <span id="place">
              <Translate contentKey="clapApplicationApp.paintingOrSculpture.place">Place</Translate>
            </span>
          </dt>
          <dd>{paintingOrSculptureEntity.place}</dd>
        </dl>
        <Button tag={Link} to="/painting-or-sculpture" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/painting-or-sculpture/${paintingOrSculptureEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaintingOrSculptureDetail;
